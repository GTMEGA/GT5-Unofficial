plugins {
    id("fpgradle-minecraft") version ("0.14.1")
}

group = "gtmega"

minecraft_fp {
    java {
        compatibility = jabel
    }

    mod {
        modid = "gregtech"
        name = "GregTech"
        rootPkg = "gregtech"
    }

    mixin {
        hasMixinDeps = true
    }

    core {
        accessTransformerFile = "gregtech_at.cfg"
    }

    tokens {
        tokenClass = "Tags"
    }

    publish {
        maven {
            repoUrl = "https://mvn.falsepattern.com/gtmega_releases/"
            repoName = "mega"
            artifact = "gt5u-mc1.7.10"
        }
    }
}

repositories {
    ic2EX()
    cursemavenEX()
    exclusive(mavenpattern(), "com.falsepattern")
    exclusive(mega(), "mega", "gtmega", "team.chisel") {
        includeModule("codechicken", "notenoughitems-mc1.7.10")
        includeModule("codechicken", "codechickencore-mc1.7.10")
        includeModule("codechicken", "forgemultipart-mc1.7.10")
    }
    exclusive(ivy("https://mvn.falsepattern.com/releases/mirror", "[orgPath]/[artifact]-[revision].[ext]"), "mirror", "mirror.micdoodle")
    exclusive(maven("usrv", "https://mvn.falsepattern.com/usrv"), "eu.usrv")
    exclusive(maven("tterrag", "https://mvn.falsepattern.com/tterrag"), "com.enderio")
    exclusive(maven("covers1624", "https://mvn.falsepattern.com/covers1624")) {
        includeModule("codechicken", "Translocator")
    }
    exclusive(maven("overmind", "https://mvn.falsepattern.com/overmind"), "com.mod-buildcraft", "com.azanor.baubles")
}

dependencies {
    implementation("mega:structurelib-mc1.7.10:1.4.1-mega:dev")
    implementation("codechicken:codechickencore-mc1.7.10:1.4.0-mega:dev")
    devOnlyNonPublishable("codechicken:notenoughitems-mc1.7.10:2.3.0-mega:dev")

    implementation("eu.usrv:YAMCore:1.7.10-0.5.80:deobf")

    devOnlyNonPublishable("mega:projectred-mc1.7.10:5.0.0-mega:dev")
    compileOnly("mega:forestry-mc1.7.10:4.5.3-mega:dev") {
        exclude("com.github.GTNewHorizons", "BuildCraft")
    }
    devOnlyNonPublishable("gtmega:ae2-mc1.7.10:rv3-beta.58-gtmega:dev")
    devOnlyNonPublishable("team.chisel:chisel-mc1.7.10:2.14.7-mega:dev")
    devOnlyNonPublishable("com.falsepattern:falsetweaks-mc1.7.10:3.9.6:dev")

    //Third-party deps

    //EnderCore 0.2.0.39_beta
    compileOnly(deobfCurse("endercore-231868:2331048"))

    //RailCraft 9.12.2.1
    compileOnly(deobfCurse("railcraft-51195:2458987"))

    //micdoodle8.com went down
//    compileOnly(rfg.deobf("mirror.micdoodle:GalacticraftCore:1.7-3.0.12.504"))

    //AppleCore 3.1.1
    compileOnly(deobfCurse("applecore-224472:2530879"))

    //TiCon 1.8.8
//    compileOnly(deobfCurse("tconstruct-74072:2264246"))

    compileOnly("com.enderio:EnderIO:1.7.10-2.3.0.417_beta:dev") {
        excludeDeps()
    }

    devOnlyNonPublishable("com.mod-buildcraft:buildcraft:7.1.23:dev") {
        excludeDeps()
    }

    compileOnly("codechicken:Translocator:1.7.10-1.1.2.16:dev") {
        excludeDeps()
    }

    compileOnly(deobfCurse("nuclear-control-2-236813:2464681"))

    devOnlyNonPublishable("com.azanor.baubles:Baubles:1.7.10-1.0.1.10:deobf") {
        excludeDeps()
    }

    //Plonk 10.0.4
//    runtimeOnly(deobfCurse("plonk-345779:4480767"))
}