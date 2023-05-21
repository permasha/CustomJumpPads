package ru.permasha.customjumppads;

import org.bukkit.plugin.java.JavaPlugin;
import ru.permasha.customjumppads.commands.JumpPadCommand;
import ru.permasha.customjumppads.listeners.Events;
import ru.permasha.customjumppads.managers.JumpPadManager;

public final class CustomJumpPads extends JavaPlugin {

    static CustomJumpPads instance;

    JumpPadManager manager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        manager = new JumpPadManager(this);
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getCommand("jumppad").setExecutor(new JumpPadCommand(this));
    }

    @Override
    public void onDisable() {

    }

    public static CustomJumpPads getInstance() {
        return instance;
    }

    public JumpPadManager getManager() {
        return manager;
    }
}
