package com.reliquiasdamagia.api_rm.utils;

import com.reliquiasdamagia.api_rm.entity.Category;
import com.reliquiasdamagia.api_rm.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final CategoryService categoryService;

    @PostConstruct
    public void init() {
        List<Category> categories = List.of(
                new Category(null, "Proteção"),
                new Category(null, "Limpeza Espiritual"),
                new Category(null, "Prosperidade"),
                new Category(null, "Descarrego"),
                new Category(null, "Sedução"),
                new Category(null, "Anjo Da Guarda"),
                new Category(null, "Banho dos Orixás"),
                new Category(null, "Banho de Pomba Gíria"),
                new Category(null, "Banho dos Seres Elementais "),
                new Category(null, "Outros Banhos"),
                new Category(null, "Bonecas antigas"),
                new Category(null, "Joias Antigas"),
                new Category(null, "Chaves"),
                new Category(null, "Baús e Caixa"),
                new Category(null, "Sinos"),
                new Category(null, "Castiçais"),
                new Category(null, "Moedas e Notas"),
                new Category(null, "Cálices"),
                new Category(null, "Athames"),
                new Category(null, "Esculturas"),
                new Category(null, "Relógios"),
                new Category(null, "Outras Antiguidades"),
                new Category(null, "Feitiços e kits"),
                new Category(null, "Pós Mágicos"),
                new Category(null, "Óleos Mágicos"),
                new Category(null, "Poções"),
                new Category(null, "Perfumes Mágicos"),
                new Category(null, "Fluidos"),
                new Category(null, "Outros Feitiços"),
                new Category(null, "Duendes"),
                new Category(null, "Fadas"),
                new Category(null, "Gnomos"),
                new Category(null, "Magos"),
                new Category(null, "Bruxas"),
                new Category(null, "Extraterrestre"),
                new Category(null, "Outros Seres"),
                new Category(null, "Mandrágora"),
                new Category(null, "Varinha"),
                new Category(null, "Dobby"),
                new Category(null, "Chapéu"),
                new Category(null, "Kit Bruxo"),
                new Category(null, "Chaveiro"),
                new Category(null, "Boneco"),
                new Category(null, "Outros"),
                new Category(null, "Grimórios"),
                new Category(null, "Penas"),
                new Category(null, "Pedras e Cristais"),
                new Category(null, "Incensos"),
                new Category(null, "Velas"),
                new Category(null, "Sais"),
                new Category(null, "Vassouras"),
                new Category(null, "Garrafas"),
                new Category(null, "Caldeirões"),
                new Category(null, "Outros Suprimentos")
        );

        categoryService.saveCategories(categories);
        System.out.println("Categorias inicializadas com sucesso!");
    }
}
