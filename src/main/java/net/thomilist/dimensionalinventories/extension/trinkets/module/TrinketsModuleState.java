package net.thomilist.dimensionalinventories.extension.trinkets.module;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtWriteView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ErrorReporter;
import net.thomilist.dimensionalinventories.module.base.player.PlayerModuleState;

import java.lang.reflect.Type;

public class TrinketsModuleState
    implements PlayerModuleState
{
    public NbtCompound trinketComponentNbt = new NbtCompound();

    public TrinketsModuleState()
    { }

    public TrinketsModuleState( final ServerPlayerEntity serverPlayerEntity )
    {
        this.loadFromPlayer( serverPlayerEntity );
    }

    private static void clearTrinkets( final TrinketComponent trinketComponent )
    {
        trinketComponent.getInventory()
            .forEach( ( groupKey, group ) -> group.forEach( ( slotKey, slot ) -> slot.clear() ) );
    }

    @Override
    public void applyToPlayer( final ServerPlayerEntity serverPlayerEntity )
    {
        TrinketsApi.getTrinketComponent( serverPlayerEntity ).ifPresent( trinketComponent -> {
            TrinketsModuleState.clearTrinkets( trinketComponent );
            WriteView view = NbtWriteView.create(ErrorReporter.EMPTY);
            trinketComponent.writeData(view);

//            (this.trinketComponentNbt, serverPlayerEntity.getRegistryManager());
        } );
    }

    @Override
    public void loadFromPlayer( final ServerPlayerEntity serverPlayerEntity )
    {
        this.trinketComponentNbt = new NbtCompound();
        TrinketsApi
            .getTrinketComponent( serverPlayerEntity )
            .ifPresent( trinketComponent -> trinketComponent.readData(this.trinketComponentNbt, serverPlayerEntity.getRegistryManager()) );
    }

    @Override
    public Type type()
    {
        return TrinketsModuleState.class;
    }
}
