package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    interface Spec{
        static Specification<Location> byAddressLike(String addressPattern){
            return (root, query, builder) ->
                    builder.like(builder.upper(root.get("address")), "%"+addressPattern.toUpperCase()+"%");
        }
        static Specification<Location> byCity(String city){
            return (root, query, builder) ->
                    builder.equal(root.get("city"), city);
        }
        static Specification<Location> byDeleted(){
            return (root, query, builder) ->
                    builder.equal(root.get("deleted"), false);
        }

    }
}
