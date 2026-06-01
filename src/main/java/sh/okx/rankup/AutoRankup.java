package sh.okx.rankup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class AutoRankup implements Runnable {
  private final RankupPlugin rankup;
  @Getter
  private Object task;
  @Getter
  private boolean cancelled = false;

  @Override
  public void run() {
    if (rankup.error()) {
      return;
    }

    RankupHelper helper = rankup.getHelper();
    for (Player player : Bukkit.getOnlinePlayers()) {
      if (player.hasPermission("rankup.auto")) {
        if (helper.checkRankup(player, false)) {
          helper.rankup(player);
        } else if (rankup.getPrestiges() != null && helper.checkPrestige(player, false)) {
          helper.prestige(player);
        }
      }
    }
  }

  public void start(long delay, long period) {
    this.task = sh.okx.rankup.util.SchedulerUtil.runTaskTimer(rankup, this, delay, period);
    this.cancelled = false;
  }

  public void cancel() {
    if (task != null) {
      sh.okx.rankup.util.SchedulerUtil.cancelTask(task);
      this.cancelled = true;
    }
  }
}
