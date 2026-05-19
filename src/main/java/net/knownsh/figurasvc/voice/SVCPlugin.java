package net.knownsh.figurasvc.voice;

import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent;
import de.maxhenkel.voicechat.api.events.ClientSoundEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import net.knownsh.figurasvc.EventAccessor;
import net.knownsh.figurasvc.FiguraSVC;
import net.knownsh.figurasvc.voice.event.ClientSoundEventData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import static net.knownsh.figurasvc.voice.AudioUtils.pcmLuaDecode;
import static net.knownsh.figurasvc.voice.AudioUtils.pcmLuaEncode;

public class SVCPlugin implements VoicechatPlugin {
    @Override
    public String getPluginId() {
        return FiguraSVC.PLUGIN_ID;
    }

    private static boolean shouldSkipVoiceHooks() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return true;
        }
        if (minecraft.level == null || minecraft.player == null || minecraft.getConnection() == null) {
            return true;
        }
        return minecraft.isPaused();
    }

    private static boolean isClientStateStable() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null
            && minecraft.level != null
            && minecraft.player != null
            && minecraft.getConnection() != null;
    }

    private static Avatar getAvatarIfReady(java.util.UUID playerId) {
        if (playerId == null || shouldSkipVoiceHooks()) {
            return null;
        }
        Avatar avatar = AvatarManager.getAvatarForPlayer(playerId);
        if (avatar == null || avatar.luaRuntime == null || !avatar.loaded) {
            return null;
        }
        return avatar;
    }

    private static short[] coerceAudio(Varargs newPCM, short[] fallback) {
        if (newPCM == null) {
            return fallback;
        }
        LuaValue first = newPCM.arg1();
        if (!(first instanceof LuaTable newPCMTable)) {
            return fallback;
        }
        try {
            return pcmLuaDecode(newPCMTable);
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static void applyAudioOverride(ClientSoundEvent event, Varargs newPCM) {
        if (!isClientStateStable()) {
            return;
        }
        short[] updated = coerceAudio(newPCM, event.getRawAudio());
        if (updated != null) {
            event.setRawAudio(updated);
        }
    }

    private static void applyAudioOverride(ClientReceiveSoundEvent.EntitySound event, Varargs newPCM) {
        if (!isClientStateStable()) {
            return;
        }
        short[] updated = coerceAudio(newPCM, event.getRawAudio());
        if (updated != null) {
            event.setRawAudio(updated);
        }
    }

    private static LuaEvent getEvent(EventAccessor accessor, boolean host) {
        return host ? accessor.FiguraSVC$getHostMicrophoneEvent() : accessor.FiguraSVC$getMicrophoneEvent();
    }

    private static LuaEvent getHostEventData(EventAccessor accessor) {
        return accessor.FiguraSVC$getHostMicrophoneEventData();
    }

    private static boolean hasListeners(LuaEvent event) {
        return event != null && event.__len() > 0;
    }

    private static java.util.UUID getLocalPlayerId() {
        try {
            return FiguraMod.getLocalPlayerUUID();
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static Varargs runAvatarEvent(Avatar avatar, LuaEvent event, Object... args) {
        if (avatar == null || !hasListeners(event) || !isClientStateStable()) {
            return null;
        }
        try {
            return avatar.run(event, avatar.render, args);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void runLegacyEvent(Avatar avatar, ClientSoundEvent event) {
        if (avatar == null || event.getVoicechat().isMuted()) {
            return;
        }
        LuaEvent legacyMicrophoneEvent = ((EventAccessor) avatar.luaRuntime.events).FiguraSVC$getMicrophoneEventLegacy();
        if (!hasListeners(legacyMicrophoneEvent)) {
            return;
        }
        LuaTable pcmTable = pcmLuaEncode(event.getRawAudio());
        try {
            avatar.run(legacyMicrophoneEvent, avatar.tick, pcmTable);
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(ClientSoundEvent.class, this::onLocalPlayerSpeak);
        registration.registerEvent(ClientReceiveSoundEvent.EntitySound.class, this::onLocalPlayerReceive);
    }

    /**
     * Event handler for when the local player receives a sound from another player.
     *
     * @param event the ClientReceiveSoundEvent containing the sound data.
     */
    private void onLocalPlayerReceive(ClientReceiveSoundEvent.EntitySound event) {
        Avatar speakingPlayer = getAvatarIfReady(event.getId());
        if (speakingPlayer == null) {
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        Player player = level.getPlayerByUUID(event.getId());
        if (player == null) {
            return;
        }

        LuaEvent microphoneEvent = getEvent((EventAccessor) speakingPlayer.luaRuntime.events, false);
        Varargs newPCM = runAvatarEvent(
            speakingPlayer,
            microphoneEvent,
            LuaString.valueOf(player.getName().getString()),
            pcmLuaEncode(event.getRawAudio())
        );
        applyAudioOverride(event, newPCM);
    }

    /**
     * Event handler for when the local player speaks.
     *
     * @param event the ClientSoundEvent containing the sound data.
     */
    private void onLocalPlayerSpeak(ClientSoundEvent event) {
        Avatar localPlayer = getAvatarIfReady(getLocalPlayerId());
        runLegacyEvent(localPlayer, event);
        if (localPlayer == null) {
            return;
        }

        EventAccessor accessor = (EventAccessor) localPlayer.luaRuntime.events;
        LuaEvent microphoneEvent = getEvent(accessor, true);
        Varargs newPCM = runAvatarEvent(localPlayer, microphoneEvent, pcmLuaEncode(event.getRawAudio()));
        applyAudioOverride(event, newPCM);

        LuaEvent microphoneEventData = getHostEventData(accessor);
        runAvatarEvent(localPlayer, microphoneEventData, new ClientSoundEventData(event));
    }
}
