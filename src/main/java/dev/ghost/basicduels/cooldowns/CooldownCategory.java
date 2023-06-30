package dev.ghost.basicduels.cooldowns;

import java.util.Objects;
import java.util.UUID;

public class CooldownCategory
{
    private final UUID id;

    public CooldownCategory()
    {
        this.id = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        CooldownCategory that = (CooldownCategory) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
