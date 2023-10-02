package xyz.epicebic.ebiclib.item;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {
    private static final String TEXTURE_URL = "http://textures.minecraft.net/texture/";

    private ItemStack item;
    private ItemMeta meta;

    private ItemBuilder() {
    }

    public static ItemBuilder create(Material material) {
        return create(material, 1);
    }

    public static ItemBuilder create(Material material, int amount) {
        ItemBuilder builder = new ItemBuilder();
        builder.item = new ItemStack(material, amount);
        builder.meta = builder.item.getItemMeta();

        return builder;
    }

    public static ItemBuilder modifyItem(ItemStack item) {
        ItemBuilder builder = new ItemBuilder();
        builder.item = item;
        builder.meta = item.getItemMeta();

        return builder;
    }

    public static ItemBuilder modifyClone(ItemStack item) {
        return modifyItem(item.clone());
    }

    public <T extends ItemMeta> ItemBuilder modify(Class<T> metaType, Consumer<T> action) {
        if (!metaType.isAssignableFrom(meta.getClass())) {
            return this;
        }

        action.accept(metaType.cast(meta));
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);

        return this;
    }

    public ItemBuilder name(String name) {
        return name(name, false);
    }

    public ItemBuilder name(String name, boolean append) {
        if (append) {
            this.meta.setDisplayName(this.meta.getDisplayName() + name);
        } else {
            this.meta.setDisplayName(name);
        }
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> itemLore = getLore();
        itemLore.addAll(lore);

        meta.setLore(itemLore);
        return this;
    }

    public ItemBuilder lore(String... lore) {
        List<String> itemLore = getLore();
        List<String> loreList = Arrays.stream(lore).toList();
        itemLore.addAll(loreList);

        meta.setLore(itemLore);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder storedEnchantment(Enchantment enchantment, int level) {
        return modify(EnchantmentStorageMeta.class, meta -> meta.addStoredEnchant(enchantment, level, true));
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantmentAndLevel) {
        for (Map.Entry<Enchantment, Integer> entry : enchantmentAndLevel.entrySet()) {
            this.meta.addEnchant(entry.getKey(), entry.getValue(),true);
        }
        return this;
    }

    public ItemBuilder unbreakable() {
        this.meta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        this.meta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public ItemBuilder modelData(int index) {
        this.meta.setCustomModelData(index);
        return this;
    }

    public ItemBuilder modifier(Attribute attribute, AttributeModifier modifier) {
        this.meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public ItemBuilder skullTexture(String texture) {
        return modify(SkullMeta.class, meta -> {
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(texture.getBytes()));
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(TEXTURE_URL + texture));
            } catch (MalformedURLException ignored) {
                return;
            }

            profile.setTextures(textures);
            ((SkullMeta) this.meta).setOwnerProfile(profile);
        });
    }

    public ItemBuilder skullTexture(Player player) {
        if (!(this.meta instanceof SkullMeta)) return this;
        ((SkullMeta)this.meta).setOwningPlayer(player);
        return this;
    }

    public ItemBuilder dyeColor(Color color) {
        if (!(this.meta instanceof LeatherArmorMeta))
            return this;

        ((LeatherArmorMeta) this.meta).setColor(color);
        return this;
    }

    public ItemBuilder potionColor(int red, int green, int blue) {
        if (!(this.meta instanceof PotionMeta))
            return this;

        ((PotionMeta) this.meta).setColor(Color.fromRGB(red, green, blue));
        return this;
    }

    public ItemBuilder potionData(PotionData data) {
        if (!(this.meta instanceof PotionMeta))
            return this;

        ((PotionMeta) this.meta).setBasePotionData(data);
        return this;
    }

    /**
     * Adds a custom potion effect to the item.
     *
     * @param effect    the potion effect to add
     * @return          the ItemBuilder instance
     */
    public ItemBuilder potionEffect(PotionEffect effect) {
        if (!(this.meta instanceof PotionMeta))
            return this;

        ((PotionMeta) this.meta).addCustomEffect(effect, true);
        return this;
    }

    /**
     * Adds multiple custom potion effects to the item.
     *
     * @param effects   the potion effects to add
     * @return          the ItemBuilder instance
     */
    public ItemBuilder potionEffects(PotionEffect... effects) {
        if (!(this.meta instanceof PotionMeta potionMeta))
            return this;

        for (PotionEffect effect : effects) {
            potionMeta.addCustomEffect(effect, true);
        }

        return this;
    }

    public ItemStack build() {
        ItemStack finalItem = this.item;
        finalItem.setItemMeta(this.meta);

        return finalItem;
    }

    private List<String> getLore() {
        return meta.hasLore() ? meta.getLore() : new ArrayList<>();
    }
}