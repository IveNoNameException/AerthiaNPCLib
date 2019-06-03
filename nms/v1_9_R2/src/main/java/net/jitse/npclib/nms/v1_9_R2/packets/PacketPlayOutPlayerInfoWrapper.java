/*
 * Copyright (c) 2018 Jitse Boonstra
 */

package net.jitse.npclib.nms.v1_9_R2.packets;

import com.comphenix.tinyprotocol.Reflection;
import com.mojang.authlib.GameProfile;
import net.jitse.npclib.api.wrapper.GameProfileWrapper;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_9_R2.WorldSettings;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * @author Jitse Boonstra
 */
public class PacketPlayOutPlayerInfoWrapper {

    private final Class<?> packetPlayOutPlayerInfoClazz = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo");
    private final Class<?> playerInfoDataClazz = Reflection.getMinecraftClass("PacketPlayOutPlayerInfo$PlayerInfoData");
    private final Reflection.ConstructorInvoker playerInfoDataConstructor = Reflection.getConstructor(playerInfoDataClazz,
            packetPlayOutPlayerInfoClazz, GameProfile.class, int.class, WorldSettings.EnumGamemode.class, IChatBaseComponent.class);

    public PacketPlayOutPlayerInfo create(PacketPlayOutPlayerInfo.EnumPlayerInfoAction action, GameProfileWrapper gameProfileWrapper, String name) {
        GameProfile gameProfile = (GameProfile) gameProfileWrapper.getGameProfile();

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo();
        Reflection.getField(packetPlayOutPlayerInfo.getClass(), "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class)
                .set(packetPlayOutPlayerInfo, action);

        Object playerInfoData = playerInfoDataConstructor.invoke(packetPlayOutPlayerInfo,
                gameProfile, 1, WorldSettings.EnumGamemode.NOT_SET,
                IChatBaseComponent.ChatSerializer.b("{\"text\":\"" + ChatColor.BLUE + "[NPC] " + name + "\"}")
        );

        Reflection.FieldAccessor<List> fieldAccessor = Reflection.getField(packetPlayOutPlayerInfo.getClass(), "b", List.class);
        List list = fieldAccessor.get(packetPlayOutPlayerInfo);
        list.add(playerInfoData);
        fieldAccessor.set(packetPlayOutPlayerInfo, list);

        return packetPlayOutPlayerInfo;
    }

}