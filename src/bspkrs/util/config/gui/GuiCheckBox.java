package bspkrs.util.config.gui;

import bspkrs.client.util.HUDUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/** @deprecated */
@Deprecated
public class GuiCheckBox extends GuiButton {
   private boolean isChecked;
   private int boxWidth;
   private int packedFGColour = 0;

   public GuiCheckBox(int id, int xPos, int yPos, String displayString, boolean isChecked) {
      super(id, xPos, yPos, displayString);
      this.isChecked = isChecked;
      this.boxWidth = 11;
      this.height = 11;
      this.width = this.boxWidth + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(this.visible) {
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.boxWidth && mouseY < this.yPosition + this.height;
         HUDUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46, this.boxWidth, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
         this.mouseDragged(mc, mouseX, mouseY);
         int color = 14737632;
         if(this.packedFGColour != 0) {
            color = this.packedFGColour;
         } else if(!this.enabled) {
            color = 10526880;
         }

         if(this.isChecked) {
            this.drawCenteredString(mc.fontRenderer, "x", this.xPosition + this.boxWidth / 2 + 1, this.yPosition + 1, 14737632);
         }

         this.drawString(mc.fontRenderer, this.displayString, this.xPosition + this.boxWidth + 2, this.yPosition + 2, color);
      }

   }

   public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {
      if(this.enabled && this.visible && p_146116_2_ >= this.xPosition && p_146116_3_ >= this.yPosition && p_146116_2_ < this.xPosition + this.width && p_146116_3_ < this.yPosition + this.height) {
         this.isChecked = !this.isChecked;
         return true;
      } else {
         return false;
      }
   }

   public boolean isChecked() {
      return this.isChecked;
   }

   public void setIsChecked(boolean isChecked) {
      this.isChecked = isChecked;
   }
}
