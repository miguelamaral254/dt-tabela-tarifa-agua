package gruporas.dttabelatarifaagua.user.core.model;

import gruporas.dttabelatarifaagua.shared.pagination.Filter;
import gruporas.dttabelatarifaagua.shared.pagination.Pageable;
import lombok.Getter;

@Getter
public class UserFilter extends Filter {
    private final String username;

    public UserFilter(String username, Pageable pageable) {
        super(pageable);
        this.username = username;
    }

    @Override
    public int getMaxPageSize() {
        return 50;
    }
}
