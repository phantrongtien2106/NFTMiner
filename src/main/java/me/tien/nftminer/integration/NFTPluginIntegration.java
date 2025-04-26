package me.tien.nftminer.integration;

import com.minecraft.nftplugin.service.MintNFTService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

public class NFTPluginIntegration {

    private final JavaPlugin plugin;
    private Plugin nftPluginInstance;
    private MintNFTService mintNFTService;

    private boolean nftPluginAvailable = false;

    public NFTPluginIntegration(JavaPlugin plugin) {
        this.plugin = plugin;
        initialize();
    }

    private void initialize() {
        try {
            nftPluginInstance = Bukkit.getPluginManager().getPlugin("NFTPlugin");
            if (nftPluginInstance == null || !nftPluginInstance.isEnabled()) {
                plugin.getLogger().warning("NFTPlugin không khả dụng hoặc chưa bật.");
                return;
            }

            connectMintNFTService();
            nftPluginAvailable = (mintNFTService != null);

            if (nftPluginAvailable) {
                plugin.getLogger().info("Đã kết nối thành công với NFTPlugin!");
            } else {
                plugin.getLogger().warning("Không thể kết nối MintNFTService từ NFTPlugin.");
            }

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Lỗi khi kết nối NFTPlugin:", e);
        }
    }

    private void connectMintNFTService() {
        try {
            Method getMintNFTServiceMethod = nftPluginInstance.getClass().getMethod("getMintNFTService");
            Object service = getMintNFTServiceMethod.invoke(nftPluginInstance);

            if (service instanceof MintNFTService) {
                mintNFTService = (MintNFTService) service;
                plugin.getLogger().info("MintNFTService đã được lấy thành công từ NFTPlugin.");
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Không thể lấy MintNFTService: " + e.getMessage());
        }
    }

    public boolean isNFTPluginAvailable() {
        return nftPluginAvailable;
    }

    public MintNFTService getMintNFTService() {
        return mintNFTService;
    }
}
