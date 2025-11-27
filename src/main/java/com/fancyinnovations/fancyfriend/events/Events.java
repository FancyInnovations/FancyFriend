package com.fancyinnovations.fancyfriend.events;

import com.fancyinnovations.fancyfriend.FancyFriend;
import com.fancyinnovations.fancyfriend.utils.Modrinth;
import com.fancyinnovations.fancyfriend.utils.PingWarnings;
import com.fancyinnovations.fancyfriend.utils.Staff;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Events extends ListenerAdapter {
    private final Path pingFilePath = Path.of("data/ping-settings.json");

    private final String geyserMsg = "The plugin may not work properly with Geyser as it is not officially supported. Additionally, display entities and other features don't even exist on Bedrock Edition.";
    private final String viaMsg = "The plugin may not work properly with Via plugins as they are not officially supported. Additionally, display entities and other features don't even exist on certain older Minecraft versions.";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        FancyFriend.logger().info("Slash command used: " + event.getName() + " by " + event.getMember().getEffectiveName());

        event.deferReply(event.getName().equalsIgnoreCase("noping")).queue();

        switch (event.getName()) {
            case "logs" -> event.getHook().editOriginal("Please use **[pastes.dev](<https://pastes.dev/>)**, **[mclo.gs](<https://mclo.gs/>)** or a similar service to upload your server logs.")
                .queue();

            case "bedrock" -> event.getHook().editOriginal("Bedrock Edition is **not** supported and never will be. Our plugins are designed exclusively for Java Edition servers.").queue();

            case "blankline" -> event.getHook().editOriginal(
                    "To add a blank line in a hologram, use `<r>` on a new line.").queue();

            case "clickable" -> event.getHook().editOriginal(
                            "Holograms currently aren't clickable themselves, but [here's](<https://docs.fancyinnovations.com/fancyholograms/tutorials/clickable-holograms/>) a workaround.")
                    .queue();

            case "converters" -> {
                String fhMessage = "As of FancyHolograms v2.4.2, holograms can be converted from DecentHolograms using `/fancyholograms convert DecentHolograms *`";
                String npcMessage = "NPC converters currently are not available. You will need to manually convert your data for the time being.";

                String message;
                if (event.getChannel().getName().contains("holograms")) message = fhMessage;
                else if (event.getChannel().getName().contains("npcs")) message = npcMessage;
                else message = fhMessage + "\n\n" + npcMessage;

                event.getHook().editOriginal(message).queue();
            }

            case "docs" -> event.getHook().editOriginal(
                    "Here are the FancyPlugins docs: <https://docs.fancyinnovations.com/>").queue();

            case "dw" -> event.getHook().editOriginal(
                            "\"Didn't/doesn't work\" explains almost nothing. Please provide info (screenshots, error messages, etc) for proper help.")
                    .queue();

            case "fixed" -> event.getHook().editOriginal("""
                            To make a hologram not rotate, the billboarding must be set to FIXED.
                            Example: `/holo edit <hologram> billboard FIXED`
                            Once complete, you must set the hologram's rotation with the `rotate` and `rotatepitch` commands.""")
                    .queue();

            case "geyser" -> event.getHook().editOriginal(geyserMsg).queue();

            case "interactions" -> event.getHook().editOriginal(
                            "See [this page](<https://docs.fancyinnovations.com/fancynpcs/tutorials/action-system/>) on how to add interactions to an NPC.")
                    .queue();

            case "manual-holo" -> event.getHook().editOriginal("""
                    ### To manually edit a hologram:
                    1. Run `/fancyholograms save`
                    2. Back up the `holograms.yml` file in case something goes wrong
                    3. Edit your `holograms.yml` file as desired
                    4. Run `/fancyholograms reload` after saving the file""").queue();

            case "multiline" -> event.getHook().editOriginal(
                            "See [this page](<https://docs.fancyinnovations.com/fancynpcs/tutorials/multiple-lines/>) on how to make an NPC name have multiple lines.")
                    .queue();

            case "noping" -> noPing(event);

            case "per-line" -> event.getHook().editOriginal(
                            "Per-line settings (such as scale or background) are not supported in FancyHolograms due to a limitation with display entities. A separate hologram will need to be created for each line.")
                    .queue();

            case "versions" -> {
                JSONObject project = new Modrinth().getProjectInfo(event.getOption("plugin").getAsString());

                List<Object> versionList = project.getJSONArray("game_versions").toList();
                String versions = versionList.getFirst().toString() + " → " + versionList.getLast()
                        .toString();

                StringBuilder message = new StringBuilder("The **" + project.getString("title") + "** plugin supports Minecraft **" + versions + "** on the following software:");

                for (Object loader : project.getJSONArray("loaders").toList()) {
                    String name = loader.toString();
                    // Capitalize the first letter of the loader name
                    message.append("\n- ").append(name.substring(0, 1).toUpperCase())
                            .append(name.substring(1));
                }

                if (project.getString("status").equals("archived")) message.append(
                        "\n*Note: This plugin is no longer maintained.*");

                event.getHook().editOriginal(message.toString()).queue();
            }

            case "via" -> event.getHook().editOriginal(viaMsg).queue();
            case "plugin_compatibility" -> event.getHook().editOriginalEmbeds(new EmbedBuilder()
                    .setTitle("Plugin Compatibility")
                    .setDescription("""
                            There is no compatibility guarantee for these plugins:
                            - **[ViaVersion](https://github.com/ViaVersion/ViaVersion)**, **[ViaBackwards](https://github.com/ViaVersion/ViaBackwards)** and **[ViaRewind](https://github.com/ViaVersion/ViaRewind)**
                              Clients on versions above 1.19.4 should not experience too much issues but some may still occur. Any problems specific to a certain client version are not going to be addressed here, but should instead be reported to Via.
                            - **[Geyser](https://github.com/GeyserMC/Geyser)**
                              Java and Bedrock are two completely different platforms. Holograms and NPCs are more than likely to be displayed incorrectly for Bedrock users. No effort will be put into fixing these issues, as they usually originate from Geyser.
                            """)
                    .setColor(0xFC5F5F)
                    .build()).queue();
            case "software_compatibility" -> event.getHook().editOriginalEmbeds(new EmbedBuilder()
                    .setTitle("Software Compatibility")
                    .setDescription("""
                            Our plugins are tested only on **[Paper](https://github.com/PaperMC/Paper)** and **[Folia](https://github.com/PaperMC/Folia)**. Before reporting any issue, please make sure you are able to **reproduce it** on a **supported** server software.
                            ### Additional Notes
                            - **[Folia](<https://github.com/PaperMC/Folia>)**
                              Folia must be compiled against Mojang mappings. Otherwise plugin won't load.
                            - **[Purpur](<https://github.com/PurpurMC/Purpur>)**
                              Tested by the community and confirmed to be fully compatible.
                            - **[Pufferfish](<https://github.com/pufferfish-gg/Pufferfish>)**
                              Older versions of Pufferfish (<= 1.19.4) may not fully support Paper plugins. Recent builds should work without any issues.
                            - **[Leaf](<https://github.com/Winds-Studio/Leaf>)**
                              Contains a lot of random patches that may negatively impact plugin compatibility, as well as bring number of other unrelated problems.
                            - **[Mohist](<https://github.com/MohistMC/Mohist>)**, **[ArcLight](https://github.com/IzzelAliz/Arclight)** and other hybrid server software...
                              Hybrid servers are not, and will never be supported. They're known for causing a lot of problems.
                            - Anything else remains a mystery. If you have some insights, let us know.
                            """)
                    .setColor(0xFC5F5F)
                    .build()).queue();
            case "proxy_compatibility" -> event.getHook().editOriginalEmbeds(new EmbedBuilder()
                    .setTitle("Proxy Compatibility")
                    .setDescription("""
                            Our plugins should be fully compatible with all proxies, however - there is a known issue with **[BungeeCord](https://github.com/SpigotMC/BungeeCord)** occasionally kicking players with "team xyz already exists" message.
                            
                            It was identified as a BungeeCord issue that was left without a clear resolution. We are still looking for a workaround, so if you have any suggestions - please let us know.
                            
                            This problem does not occur on **[Velocity](https://github.com/PaperMC/Velocity)**, which we recommend using instead.
                            """)
                    .setColor(0xFC5F5F)
                    .build()).queue();
            case "need_more_info" -> event.getHook().editOriginalEmbeds(new EmbedBuilder()
                    .setTitle("More Information Needed")
                    .setDescription("""
                            We need more information to help with your issue. Please provide:
                            
                            1. **Server Software and Version**
                              Send a screenshot or copy-paste the output of `/ver` command.
                            2. **Plugin Version**
                              Send a screenshot or copy-paste the output of `/ver PluginName` command.
                            3. **Plugins List** (Upon Request)
                              Send a screenshot or copy-paste the output of `/plugins` command.
                            4. **Server Logs** (Upon Request)
                              Logs can be uploaded to **[pastes.dev](<https://pastes.dev/>)**, **[mclo.gs](<https://mclo.gs/>)** or a similar service.
                            
                            If you haven't already, make sure to describe the issue in as much detail as possible.
                            """)
                    .setColor(0xFC5F5F)
                    .build()).queue();

        }

//        BotAnalytics.get().getClient().getEventService().createEvent(
//                BotAnalytics.get().getProjectId(),
//                new Event("CommandExecuted", Map.of("command", event.getName())));
    }

    private void noPing(SlashCommandInteractionEvent event) {
        if (!Staff.isStaff(event.getMember())) {
            event.getHook().editOriginal("Invalid permissions!").queue();
            return;
        }

        try {
            JSONObject pingSettings = new JSONObject(Files.readString(pingFilePath));

            switch (event.getOption("option").getAsString()) {
                case "all" -> {
                    pingSettings.remove(String.valueOf(event.getMember().getIdLong()));
                    Files.writeString(pingFilePath, pingSettings.toString());
                    event.getHook().editOriginal("You are now protected from all pings.").queue();
                }

                case "explicit" -> {
                    pingSettings.put(String.valueOf(event.getMember().getIdLong()), 1);
                    Files.writeString(pingFilePath, pingSettings.toString());
                    event.getHook().editOriginal("You are now protected from explicit pings only.").queue();
                }

                case "off" -> {
                    pingSettings.put(String.valueOf(event.getMember().getIdLong()), 0);
                    Files.writeString(pingFilePath, pingSettings.toString());
                    event.getHook().editOriginal("You are no longer protected from pings.").queue();
                }
            }
        } catch (IOException e) {
            FancyFriend.logger().error("Failed to update ping settings");
            FancyFriend.logger().error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Ignore bots
        if (event.getAuthor().isBot()) return;
        // Make sure the channel's in a guild
        if (!event.getMessage().getChannelType().isGuild()) return;
        // Ignore staff
        if (Staff.isStaff(event.getMember())) return;

        Message message = event.getMessage();

        String strippedMsg = message.getContentStripped().toLowerCase();
        String spacelessMsg = strippedMsg.replace(" ", "");
        // If someone asks if a plugin works with Via or Geyser
        if (strippedMsg.contains("work") || strippedMsg.contains("support")) if (spacelessMsg.contains(
                "viaversion") || spacelessMsg.contains("viabackwards")) message.reply(viaMsg).queue();
        else if (strippedMsg.contains("geyser")) message.reply(geyserMsg).queue();

        // Ping check
        int count = 0;
        // Iterate through each pinged member
        for (Member member : message.getMentions().getMembers())
            if (Staff.isStaff(member)) try {
                Long staffId = member.getIdLong();
                JSONObject pingSettings = new JSONObject(Files.readString(pingFilePath));
                // Default to -1 if the user doesn't have a setting
                if (pingSettings.has(String.valueOf(staffId))) {
                    // -1: all | 0: off | 1: explicit
                    int pingSetting = pingSettings.getInt(String.valueOf(staffId));
                    if (pingSetting == 0) continue;
                    if (pingSetting == 1 && message.getMessageReference() != null) continue;
                }
                count++;
            } catch (IOException e) {
                FancyFriend.logger().error("Failed to read ping settings");
                FancyFriend.logger().error(e);
                e.printStackTrace();
            }

        Long authorId = event.getAuthor().getIdLong();
        if (count > 0) {
            new PingWarnings().warn(authorId, event, count);
            StringBuilder pingMessage = new StringBuilder("Hey " + event.getMember()
                    .getEffectiveName() + ", please don't ping staff members!");

            boolean appendTicket = true;

            if (message.getChannelType() == ChannelType.TEXT)
                if (message.getCategory().getIdLong() == 1112487038160220180L) appendTicket = false;

            if (appendTicket) pingMessage.append(
                    "\nIf there's something that must be addressed privately, please make a ticket with <#1112486757359960175>");

            if (PingWarnings.warnings.get(authorId) > 1 && message.getMessageReference() != null)
                pingMessage.append("\n-# Please [turn off](https://tenor.com/view/20411479) your reply pings.");

            // Save the message without the warning for later editing
            String warningRemoved = pingMessage.toString();

            if (PingWarnings.warnings.get(authorId) > 1) pingMessage.append("\n-# Warning ").append(
                    PingWarnings.warnings.get(authorId)).append("/3");

            message.reply(pingMessage).queue(sentMsg -> sentMsg.editMessage(warningRemoved)
                    .setSuppressEmbeds(true).queueAfter(
                            15, TimeUnit.MINUTES, null, new ErrorHandler().handle(
                                    ErrorResponse.UNKNOWN_MESSAGE,
                                    (e) -> FancyFriend.logger().error("Failed to edit message - " + e.getMessage())
                            )
                    )
            );
        }
    }
}