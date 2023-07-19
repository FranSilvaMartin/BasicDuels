package dev.ghost.basicduels.cooldowns;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;

public class Cooldown
{
    private final TimeUnit unit;
    private final long cooldown;
    private final Map<Long, Pair<Runnable, Boolean>> steps = new HashMap<>();

    private long startTime = 0;

    private Runnable startRunnable;
    private Runnable cancelRunnable;
    private Runnable completeRunnable;

    private boolean cancelled = false;

    /**
     * Create a new cooldown instance
     *
     * @param time Time of the cooldown
     * @param unit TimeUnit of the time used
     */
    Cooldown(int time, TimeUnit unit)
    {
        this.cooldown = unit.toMillis(time);
        this.unit = unit;
    }

    /**
     * Set an action on cooldown start
     *
     * @param startRunnable runnable to execute when started
     * @return current cooldown instance
     */
    public Cooldown whenStarted(Runnable startRunnable)
    {
        this.startRunnable = startRunnable;
        return this;
    }

    /**
     * Set an action on cooldown cancel
     *
     * @param cancelRunnable runnable to execute when cancelled
     * @return current cooldown instance
     */
    public Cooldown whenCancelled(Runnable cancelRunnable)
    {
        this.cancelRunnable = cancelRunnable;
        return this;
    }

    /**
     * Set an action on cooldown complete
     *
     * @param completeRunnable runnable to execute on when completed
     * @return current cooldown instance
     */
    public Cooldown whenCompleted(Runnable completeRunnable)
    {
        this.completeRunnable = completeRunnable;
        return this;
    }

    /**
     * Add a step to the current cooldown
     *
     * @param time Time to execute the step (1 = after 1 second [or your time unit selected])
     * @param runnable runnable to execute
     * @return current cooldown instance
     */
    public Cooldown addStep(long time, Runnable runnable)
    {
        this.steps.put(unit.toMillis(time), new MutablePair<>(runnable, false));
        return this;
    }

    /**
     * Start the cooldown
     */
    public void start()
    {
        if (startTime == 0)
        {
            startTime = System.currentTimeMillis();
            if (startRunnable != null)
                startRunnable.run();
        }
    }

    /**
     * Cancel the cooldown
     */
    public void cancel()
    {
        if (!cancelled)
        {
            this.cancelled = true;
            if (cancelRunnable != null)
                cancelRunnable.run();
        }
    }

    /**
     * Define if the cooldown is cancelled
     *
     * @return true if cancelled false if not
     */
    public boolean isCancelled()
    {
        return cancelled;
    }

    /**
     * Define if the cooldown is completed
     *
     * @return true if completed false if not
     */
    public boolean isCompleted()
    {
        return System.currentTimeMillis() > startTime + cooldown;
    }

    /**
     * Get the time left before completion
     *
     * @return time left before completion
     */
    public long getTimeLeft()
    {
        return unit.convert(startTime + cooldown - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Method executed every X tick (defined in CooldownManager constructor)
     */
    void tick()
    {
        for (Map.Entry<Long, Pair<Runnable, Boolean>> entry : steps.entrySet())
        {
            Pair<Runnable, Boolean> pair = entry.getValue();
            if (System.currentTimeMillis() > startTime + entry.getKey())
            {
                if (!pair.getValue())
                {
                    pair.getLeft().run();
                    pair.setValue(true);
                }
            }
        }
        if (isCompleted())
        {
            if (completeRunnable != null)
                completeRunnable.run();
        }
    }
}
