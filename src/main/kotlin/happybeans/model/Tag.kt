package happybeans.model

import jakarta.persistence.*

//ver1
@Entity
@Table(name = "tags")  // recommended: be explicit about table name
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String
)

//ver2
//@Entity
//@Table(name = "tags")
//data class Tag(
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long = 0L,
//
//    @Column(name = "name", nullable = false) //, unique = true)
//    val name: String,
//
//    @Column(name = "category", nullable = false)
//    @Enumerated(EnumType.STRING)
//    val category: TagCategory,
//)
//
