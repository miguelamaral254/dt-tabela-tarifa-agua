package gruporas.dttabelatarifaagua.user.core.model;

import gruporas.dttabelatarifaagua.shared.pagination.Filter;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import gruporas.dttabelatarifaagua.user.persistence.model.Role;
import lombok.Getter;

@Getter
public class UserFilter extends Filter {
    private final Role role;

    public UserFilter(Role role, Pageable pageable) {
        super(pageable);
        this.role = role;
    }

    @Override
    public int getMaxPageSize() {
        return 50;
    }
}
