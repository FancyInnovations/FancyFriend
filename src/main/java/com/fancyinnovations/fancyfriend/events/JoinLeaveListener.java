package com.fancyinnovations.fancyfriend.events;

import com.fancyinnovations.fancyfriend.FancyFriend;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, "1134432919008587786");
        if (logChannel == null) return;

        long memberCount = FancyFriend.instance().jda().getUserCache().size();
        logChannel.sendMessage("**" + event.getUser().getName() + "**" + " has joined the server (" + memberCount + " total members)").queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, "1134432919008587786");
        if (logChannel == null) return;

        long memberCount = FancyFriend.instance().jda().getUserCache().size();
        logChannel.sendMessage("**" + event.getUser().getName() + "**" + " has left the server (" + memberCount + " total members)").queue();
    }
}