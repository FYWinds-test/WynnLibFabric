package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MainAttackDamageModifierProperty(ability: Ability, private val modifier: Int):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<MainAttackDamageModifierProperty> {
        override fun create(ability: Ability, data: JsonElement): MainAttackDamageModifierProperty {
            return MainAttackDamageModifierProperty(ability, data.asInt)
        }
        override fun getKey(): String = "main_damage_modifier"
    }

    fun getMainAttackDamageModifier(): Int = modifier

    fun getMainAttackDamageModifierRate(): Double = getMainAttackDamageModifier() / 100.0

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), signed(modifier))
    }

    override fun modify(entry: PropertyEntry) {
        MainAttackDamageProperty.from(entry)?.let {
            val damage = it.getMainAttackDamage() + getMainAttackDamageModifier()
            entry.setProperty(MainAttackDamageProperty.getKey(), MainAttackDamageProperty(it.getAbility(), damage))
        }
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAIN_ATTACK_DAMAGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${signed(modifier)}%").formatted(Formatting.WHITE)))
    }
}