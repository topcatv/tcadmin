package org.topcatv.devops.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.topcatv.devops.model.Permission;
import org.topcatv.devops.model.Role;
import org.topcatv.devops.model.User;
import org.topcatv.devops.repository.UserRepository;

import java.io.Serializable;
import java.util.*;

/**
 * @author liuyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException("用户名为空");
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new CustomUserDetails(user.getUsername(),
                user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
                user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                getAuthorities(user.getRoles()));
    }

    private static class SimpleGrantedAuthorityComparator implements
            Comparator<SimpleGrantedAuthority> {

        @Override
        public int compare(SimpleGrantedAuthority o1, SimpleGrantedAuthority o2) {
            return o1.equals(o2) ? 0 : -1;
        }
    }

    /**
     * Retrieves a collection of {@link GrantedAuthority} based on a list of
     * roles
     *
     * @param roles
     *            the assigned roles of the user
     * @return a collection of {@link GrantedAuthority}
     */
    public Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {

        Set<SimpleGrantedAuthority> authList = new TreeSet<>(
                new SimpleGrantedAuthorityComparator());

        for (Role role : roles) {
            authList.add(new SimpleGrantedAuthority(role.getName()));
            authList.addAll(getGrantedAuthorities(role));
        }

        return authList;
    }

    /**
     * Wraps a {@link Role} role to {@link SimpleGrantedAuthority} objects
     *
     * @param role
     *            {@link String} of roles
     * @return list of granted authorities
     */
    public static Set<SimpleGrantedAuthority> getGrantedAuthorities(Role role) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        List<Permission> rolePermissions = role.getPermissions();
        for (Permission permission : rolePermissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
        }

        return authorities;
    }

}
