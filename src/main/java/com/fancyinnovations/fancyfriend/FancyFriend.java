package com.fancyinnovations.fancyfriend;

import com.fancyinnovations.fancyfriend.events.BanListener;
import com.fancyinnovations.fancyfriend.events.Events;
import com.fancyinnovations.fancyfriend.events.JoinLeaveListener;
import com.fancyinnovations.fancyfriend.utils.Modrinth;
import de.oliver.fancyanalytics.logger.ExtendedFancyLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.concurrent.TimeUnit;

public class FancyFriend {

    private static ExtendedFancyLogger logger = new ExtendedFancyLogger("FancyFriend");
    private static FancyFriend instance;
    public final long GUILD_ID = 1092507996166295644L;
    private JDA jda;

    public static void main(String[] args) {
        logger.info("Starting");

        instance = new FancyFriend();
        instance.initJDA();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> new FancyFriend().shutdown(), "Shutdown Hook"));

        logger.info("Started");
    }

    public static ExtendedFancyLogger logger() {
        return logger;
    }

    public static FancyFriend instance() {
        return instance;
    }

    public void initJDA() {
        String token = System.getenv("DISCORD_BOT_TOKEN");

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE);
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setEnableShutdownHook(false);
        builder.addEventListeners(new Events());
        builder.addEventListeners(new JoinLeaveListener());
        builder.addEventListeners(new BanListener());
        jda = builder.build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            logger.error("Failed to start bot");
            logger.error(e);
            System.exit(1);
        }

        // Cache members
        jda.getGuildById(GUILD_ID).loadMembers().get();

        OptionData pingOptions = new OptionData(
                OptionType.STRING, "option", "The ping protection method to use", true).addChoices(
                new Command.Choice("All Pings", "all"),
                new Command.Choice("Explicit Only (Ignore Replies)", "explicit"),
                new Command.Choice("Off", "off"));

        jda.getGuildById(GUILD_ID).updateCommands().addCommands(
                Commands.slash("logs", "Use pastes.dev for logs"),
                Commands.slash("bedrock", "Bedrock Edition support information"),
                Commands.slash("blankline", "How to add a blank line in a hologram"),
                Commands.slash("clickable", "Clickable FancyHolograms tutorial"),
                Commands.slash("converters", "FH & FN converter messages"),
                Commands.slash("docs", "Get the FancyPlugins documentation"),
                Commands.slash("dw", "Doesn't work message"),
                Commands.slash("fixed", "Show how to set a hologram to fixed"),
                Commands.slash("geyser", "Geyser not supported message"),
                Commands.slash("interactions", "How to add interactions to an NPC"),
                Commands.slash("manual-holo", "How to manually edit a hologram properly"),
                Commands.slash("multiline", "Make an NPC name have multiple lines"),
                Commands.slash("noping", "Change the status of your ping protection").addOptions(pingOptions),
                Commands.slash("per-line", "Per-line settings not supported message"),
                Commands.slash("versions", "Get a plugin's supported MC versions")
                        .addOptions(new Modrinth().getProjects()),
                Commands.slash("via", "ViaVersion not supported message"),
                Commands.slash("plugin_compatibility", "Plugin compatibility information"),
                Commands.slash("software_compatibility", "Server compatibility information"),
                Commands.slash("proxy_compatibility", "Proxy compatibility information"),
                Commands.slash("need_more_info", "Guide user to provide more information"),
                Commands.slash("use-minimessage", "Use MiniMessage GIF"),
                Commands.slash("not-caused-by-us", "Not caused by us GIF")).queue();
    }

    public void shutdown() {
        logger.info("Shutting down");

        try {
            jda.shutdown();
            if (!jda.awaitShutdown(
                    10,
                    TimeUnit.SECONDS)) {
                jda.shutdownNow();
                jda.awaitShutdown();
            }
        } catch (NoClassDefFoundError | InterruptedException e) {
            logger.error("Failed to shutdown");
            logger.error(e);
        }

        logger.info("Shutdown complete");
    }

    public JDA jda() {
        return jda;
    }
}
