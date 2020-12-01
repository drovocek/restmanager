package edu.volkov.restmanager.repository.menuItem;

import edu.volkov.restmanager.model.MenuItem;
import edu.volkov.restmanager.repository.menu.CrudMenuRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class DataJpaMenuItemRepository implements MenuItemRepository {

    private final CrudMenuItemRepository crudMenuItemRepository;
    private final CrudMenuRepository crudMenuRepository;

    public DataJpaMenuItemRepository(
            CrudMenuItemRepository crudMenuItemRepository,
            CrudMenuRepository crudMenuRepository) {
        this.crudMenuItemRepository = crudMenuItemRepository;
        this.crudMenuRepository = crudMenuRepository;
    }

    @Transactional
    @Override
    public MenuItem save(MenuItem mItem, int menuId) {
        if (!mItem.isNew() && get(mItem.getId(), menuId) == null) {
            return null;
        }
        mItem.setMenu(crudMenuRepository.getOne(menuId));
        return crudMenuItemRepository.save(mItem);
    }

    @Override
    public boolean delete(int id, int menuId) {
        return crudMenuItemRepository.delete(id, menuId) != 0;
    }

    @Override
    public MenuItem get(int id, int menuId) {
        return crudMenuItemRepository.findById(id)
                .filter(menuItm -> menuItm.getMenu().getId() == menuId)
                .orElse(null);
    }
}
