package org.ahroo.nexus.repository.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * <a href="https://stackoverflow.com/a/33168644/10582056">source</a>
 */
@NoRepositoryBean
public interface SoftDeleteAwareJpaRepository<T, ID> extends JpaRepository<T, ID> {

    @Override
    @Query("select e from #{#entityName} e where e.isDeleted=false")
    @NotNull
    List<T> findAll();

    //Look up deleted entities
    @Query("select e from #{#entityName} e where e.isDeleted=true")
    List<T> recycleBin();

    //Soft delete.
    @Query("update #{#entityName} e set e.isDeleted=true where e.id=?1")
    @Modifying
    void softDelete(ID id);

}
