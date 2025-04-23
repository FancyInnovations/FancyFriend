package com.fancyinnovations.fancyfriend.utils;

import net.dv8tion.jda.api.entities.Member;

public class Staff {

    public static boolean isStaff(Member member) {
        // Moderator | Developer | Helpful | Contributor
        String roles = member.getRoles().toString();
        return roles.contains("1134906027142299749") || roles.contains("1092512242127339610") || roles.contains(
                "1198213765125128302") || roles.contains("1092512359886622881");
    }

}
