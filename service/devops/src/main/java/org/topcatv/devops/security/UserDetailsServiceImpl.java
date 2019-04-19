package org.topcatv.devops.security;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.topcatv.devops.model.User;
import org.topcatv.devops.repository.UserRepository;

import java.io.Serializable;

/**
 * @author liuyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username " + username + " not found");
        }
        Hibernate.initialize(user.getAuthorities());
        return user;
    }

}
