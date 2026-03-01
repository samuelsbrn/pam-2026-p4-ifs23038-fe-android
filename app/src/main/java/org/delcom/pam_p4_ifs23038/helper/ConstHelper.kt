package org.delcom.pam_p4_ifs23038.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        
        // Topic: Plants (Original)
        Plants(path = "plants"),
        PlantsAdd(path = "plants/add"),
        PlantsDetail(path = "plants/{plantId}"),
        PlantsEdit(path = "plants/{plantId}/edit"),
        
        // Topic: Motors (New)
        Motors(path = "motors"),
        MotorsAdd(path = "motors/add"),
        MotorsDetail(path = "motors/{motorId}"),
        MotorsEdit(path = "motors/{motorId}/edit"),
    }
}