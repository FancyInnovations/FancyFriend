package com.fancyinnovations.fancyfriend.events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanListener extends ListenerAdapter {

    @Override
    public void onGuildBan(GuildBanEvent event) {
        TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, "1195445607763030126");
        if (logChannel == null) return;

        logChannel.sendMessage("**" + event.getUser().getName() + "**" + " has been banned").queue();
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, "1195445607763030126");
        if (logChannel == null) return;

        logChannel.sendMessage("**" + event.getUser().getName() + "**" + " has been unbanned").queue();
    }
}