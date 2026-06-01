package sh.okx.rankup.ranksgui;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class RanksGuiListener implements Listener {

  // 使用 ConcurrentHashMap 以确保 Folia 多线程环境下的线程安全
  private final ConcurrentHashMap<Player, RanksGui> guiMap = new ConcurrentHashMap<>();

  @EventHandler
  public void on(InventoryCloseEvent event) {
    if (!(event.getPlayer() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getPlayer();
    if (guiMap.containsKey(player)) {
      RanksGui ranksGui = guiMap.get(player);
      if (ranksGui.getInventory() != null
          && ranksGui.getInventory() == event.getInventory()) {
        guiMap.remove(player);
      }
    }
  }

  @EventHandler
  public void on(InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player)) {
      return;
    }
    Player player = (Player) event.getWhoClicked();
    RanksGui ranksGui = guiMap.get(player);
    if (ranksGui != null && event.getInventory() == ranksGui.getInventory()) {
      event.setCancelled(true);
      ranksGui.click(event);
    }
  }

  public void open(RanksGui gui) {
    guiMap.put(gui.getPlayer(), gui);
    gui.open();
  }
}