package extracells.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import extracells.container.slot.SlotFake;

public abstract class ECContainer extends Container
{

	private int inventorySize;

	public ECContainer(int inventorySize)
	{
		this.inventorySize = inventorySize;
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player)
	{
		Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
		if (slot instanceof SlotFake)
		{
			return slotClickPhantom(slot, mouseButton, modifier, player);
		}
		return super.slotClick(slotNum, mouseButton, modifier, player);
	}

	private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player)
	{
		ItemStack stack = null;

		if (mouseButton == 2)
		{
			if (((SlotFake) slot).canAdjust())
			{
				slot.putStack(null);
			}
		} else if (mouseButton == 0 || mouseButton == 1)
		{
			InventoryPlayer playerInv = player.inventory;
			slot.onSlotChanged();
			ItemStack stackSlot = slot.getStack();
			ItemStack stackHeld = playerInv.getItemStack();

			if (stackSlot != null)
			{
				stack = stackSlot.copy();
			}

			if (stackSlot == null)
			{
				if (stackHeld != null && slot.isItemValid(stackHeld))
				{
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if (stackHeld == null)
			{
				adjustPhantomSlot(slot, mouseButton, modifier);
				slot.onPickupFromSlot(player, playerInv.getItemStack());
			} else if (slot.isItemValid(stackHeld))
			{
				if (canStacksMerge(stackSlot, stackHeld))
				{
					adjustPhantomSlot(slot, mouseButton, modifier);
				} else
				{
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		}
		return stack;
	}

	public boolean canStacksMerge(ItemStack stack1, ItemStack stack2)
	{
		if (stack1 == null || stack2 == null)
			return false;
		if (!stack1.isItemEqual(stack2))
			return false;
		if (!ItemStack.areItemStackTagsEqual(stack1, stack2))
			return false;
		return true;

	}

	protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier)
	{
		if (!((SlotFake) slot).canAdjust())
		{
			return;
		}
		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if (modifier == 1)
		{
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
		} else
		{
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
		}

		if (stackSize > slot.getSlotStackLimit())
		{
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if (stackSlot.stackSize <= 0)
		{
			slot.putStack((ItemStack) null);
		}
	}

	protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier)
	{
		if (!((SlotFake) slot).canAdjust())
		{
			return;
		}
		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if (stackSize > slot.getSlotStackLimit())
		{
			stackSize = slot.getSlotStackLimit();
		}
		ItemStack phantomStack = stackHeld.copy();
		phantomStack.stackSize = stackSize;

		slot.putStack(phantomStack);
	}

	protected boolean shiftItemStack(ItemStack stackToShift, int start, int end)
	{
		boolean changed = false;
		if (stackToShift.isStackable())
		{
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++)
			{
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot != null && canStacksMerge(stackInSlot, stackToShift))
				{
					int resultingStackSize = stackInSlot.stackSize + stackToShift.stackSize;
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					if (resultingStackSize <= max)
					{
						stackToShift.stackSize = 0;
						stackInSlot.stackSize = resultingStackSize;
						slot.onSlotChanged();
						changed = true;
					} else if (stackInSlot.stackSize < max)
					{
						stackToShift.stackSize -= max - stackInSlot.stackSize;
						stackInSlot.stackSize = max;
						slot.onSlotChanged();
						changed = true;
					}
				}
			}
		}
		if (stackToShift.stackSize > 0)
		{
			for (int slotIndex = start; stackToShift.stackSize > 0 && slotIndex < end; slotIndex++)
			{
				Slot slot = (Slot) inventorySlots.get(slotIndex);
				ItemStack stackInSlot = slot.getStack();
				if (stackInSlot == null)
				{
					int max = Math.min(stackToShift.getMaxStackSize(), slot.getSlotStackLimit());
					stackInSlot = stackToShift.copy();
					stackInSlot.stackSize = Math.min(stackToShift.stackSize, max);
					stackToShift.stackSize -= stackInSlot.stackSize;
					slot.putStack(stackInSlot);
					slot.onSlotChanged();
					changed = true;
				}
			}
		}
		return changed;
	}

	private boolean tryShiftItem(ItemStack stackToShift, int numSlots)
	{
		for (int machineIndex = 0; machineIndex < numSlots - 9 * 4; machineIndex++)
		{
			Slot slot = (Slot) inventorySlots.get(machineIndex);
			if (slot instanceof Slot)
			{
				continue;
			}
			if (slot instanceof SlotFake)
			{
				continue;
			}
			if (!slot.isItemValid(stackToShift))
			{
				continue;
			}
			if (shiftItemStack(stackToShift, machineIndex, machineIndex + 1))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
	{
		ItemStack originalStack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		int numSlots = inventorySlots.size();
		if (slot != null && slot.getHasStack())
		{
			ItemStack stackInSlot = slot.getStack();
			originalStack = stackInSlot.copy();
			if (slotIndex >= numSlots - 9 * 4 && tryShiftItem(stackInSlot, numSlots))
			{
				// NOOP
			} else if (slotIndex >= numSlots - 9 * 4 && slotIndex < numSlots - 9)
			{
				if (!shiftItemStack(stackInSlot, numSlots - 9, numSlots))
				{
					return null;
				}
			} else if (slotIndex >= numSlots - 9 && slotIndex < numSlots)
			{
				if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots - 9))
				{
					return null;
				}
			} else if (!shiftItemStack(stackInSlot, numSlots - 9 * 4, numSlots))
			{
				return null;
			}
			slot.onSlotChange(stackInSlot, originalStack);
			if (stackInSlot.stackSize <= 0)
			{
				slot.putStack(null);
			} else
			{
				slot.onSlotChanged();
			}
			if (stackInSlot.stackSize == originalStack.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(player, stackInSlot);
		}
		return originalStack;
	}

	public int getInventorySize()
	{
		return inventorySize;
	}
}