-- USERS
create table users (
                       id            bigserial primary key,
                       email         varchar not null,
                       password      varchar not null,
                       first_name    varchar not null,
                       last_name     varchar not null,
                       role          varchar not null,
                       created_at    timestamp default now(),
                       updated_at    timestamp default now()
);

-- TAGS
create table tags (
                      id         bigserial primary key,
                      name       varchar not null unique,
                      created_at timestamp default now()
);
create index idx_tag_name on tags(name);

-- USER <-> TAGS (likes / dislikes)
create table user_likes_tags (
                                 user_id bigint not null references users(id) on delete cascade,
                                 tag_id  bigint not null references tags(id)  on delete cascade,
                                 primary key (user_id, tag_id)
);

create table user_dislikes_tags (
                                    user_id bigint not null references users(id) on delete cascade,
                                    tag_id  bigint not null references tags(id)  on delete cascade,
                                    primary key (user_id, tag_id)
);

-- RESTAURANTS
create table restaurants (
                             id          bigserial primary key,
                             user_id     bigint references users(id) on delete set null,
                             name        varchar(100) not null,
                             description varchar(500),
                             image       varchar,
                             address_url varchar,
                             created_at  timestamp default now(),
                             updated_at  timestamp default now()
);

-- Working hours (ElementCollection)
create table restaurant_working_dates_hours (
                                                restaurant_id bigint not null references restaurants(id) on delete cascade,
                                                day_of_week   varchar not null,
                                                open_time     time not null,
                                                close_time    time not null,
                                                primary key (restaurant_id, day_of_week)
);

-- DISHES
create table dishes (
                        id          bigserial primary key,
                        restaurant_id bigint references restaurants(id) on delete cascade,
                        name        varchar not null,
                        description varchar not null,
                        image       varchar not null,
                        created_at  timestamp default now(),
                        updated_at  timestamp default now()
);

-- DISH OPTIONS
create table dish_options (
                              id               bigserial primary key,
                              dish_id          bigint references dishes(id) on delete cascade,
                              name             varchar(100) not null,
                              description      varchar(500),
                              price            double precision not null,
                              image            varchar not null,
                              available        boolean not null default true,
                              prep_time_minute integer not null default 0,
                              created_at       timestamp default now(),
                              updated_at       timestamp default now()
);

-- DISH_OPTION <-> TAG
create table dish_option_tags (
                                  dish_option_id bigint not null references dish_options(id) on delete cascade,
                                  tag_id         bigint not null references tags(id)         on delete cascade,
                                  primary key (dish_option_id, tag_id)
);

-- CART PRODUCTS
create table cart_products (
                               id              bigserial primary key,
                               user_id         bigint not null references users(id) on delete cascade,
                               dish_id         bigint not null references dishes(id) on delete restrict,
                               dish_option_id  bigint not null references dish_options(id) on delete restrict,
                               quantity        integer not null,
                               created_at      timestamp default now(),
                               updated_at      timestamp default now(),
                               constraint uq_cart_user_option unique (user_id, dish_option_id)
);

-- DISH REVIEWS (без явных FK в сущности — оставляю как денормализованный снэпшот)
create table dish_reviews (
                              id                bigserial primary key,
                              user_id           bigint not null,
                              user_name         varchar not null,
                              rating            double precision not null,
                              message           varchar,
                              dish_option_id    bigint,
                              dish_option_name  varchar,
                              dish_option_price double precision,
                              created_at        timestamp default now(),
                              updated_at        timestamp default now()
);

-- JOIN REQUESTS
create table join_request (
                              id         bigserial primary key,
                              email      varchar,
                              first_name varchar,
                              last_name  varchar,
                              message    varchar,
                              status     varchar not null,
                              created_at timestamp default now()
);

-- ORDERS
create table orders (
                        id              bigserial primary key,
                        user_id         bigint not null,
                        user_email      varchar not null,
                        payment_id      varchar not null,
                        total_amount    double precision not null default 0,
                        status          varchar not null,
                        creation_source varchar not null,
                        created_at      timestamp default now(),
                        updated_at      timestamp default now()
);

-- ORDER PRODUCTS (owning side: orders_id)
create table order_product (
                               id             bigserial primary key,
                               orders_id      bigint not null references orders(id) on delete cascade,
                               dish_option_id bigint,
                               dish_name      varchar not null,
                               price          double precision not null,
                               quantity       integer not null,
                               created_at     timestamp default now(),
                               updated_at     timestamp default now()
);

-- PAYMENTS
create table payment (
                         id             bigserial primary key,
                         session_id     varchar not null,
                         amount         double precision not null,
                         order_id       bigint not null references orders(id) on delete cascade,
                         status         varchar not null,
                         payment_option varchar not null,
                         created_at     timestamp default now(),
                         updated_at     timestamp default now()
);

-- RESTAURANT REVIEWS (как и DishReview — снэпшот без явных связей)
create table restaurant_review (
                                   id              bigserial primary key,
                                   user_id         bigint not null,
                                   user_name       varchar not null,
                                   rating          double precision not null,
                                   message         varchar,
                                   restaurant_id   bigint,
                                   restaurant_name varchar,
                                   created_at      timestamp default now(),
                                   updated_at      timestamp default now()
);
