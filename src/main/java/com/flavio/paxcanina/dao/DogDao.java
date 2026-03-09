// com.flavio.paxcanina.dao.DogDao
package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.dto.DogAdminListDto;
import com.flavio.paxcanina.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DogDao extends JpaRepository<Dog, Integer> {
    List<Dog> findByOwnerIdUser(Integer idUser);

    @Query("""
select new com.flavio.paxcanina.dto.DogAdminListDto(
    d.idDog,
    d.name,
    d.birthDate,
    d.sex,
    b.name,
    concat(o.firstName, ' ', o.lastName)
)
from Dog d
left join d.breed b
left join d.owner o
order by d.name
""")
    List<DogAdminListDto> findAllForAdminDto();
}
