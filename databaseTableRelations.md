// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table auction_tbl {
auction_id bigint [primary key]
auction_date datetime
auction_name varchar
auction_time varchar
base_bid bidint
bid_increase_by bigint
max_player_per_team bigint
min_player_per_team bigint
points_per_team bigint
reserve bigint
season int
ua_fk bigint [ref: > user_tbl.user_id]
}

Table user_tbl {
user_id bigint [primary key]
name varchar
email varchar
city varchar
mobile_no bigint
password varchar
}

Table player_tbl {
player_id bigint [primary key]
player_name varchar
details  varchar
form_no integer
jerssey_name varchar
jerssey_number integer
mobile_no varchar
player_age int
player_style tinyint
t_shirt_size varchar
trouser_size varchar
cp_fk binary [ref: - category_tbl.category_id]
}

Ref auction_player_tbl: auction_tbl.auction_id <> player_tbl.player_id

Table category_tbl{
category_id bigint [primary key]
base_bid bigint
category_name varchar
increment bigint
max_player_per_team bigint
min_player_per_team bigint
ac_fk bigint [ref: < auction_tbl.auction_id]
}

Table team_tbl{
team_id bigint [primary key]
short_name varchar
shortcut_key varchar
team_name varchar
at_fk bigint [ref: < auction_tbl.auction_id]
}

Table additional_increments{
id binary
after bigint
increment nigint
auction_id bigint [ref: < auction_tbl.auction_id]
}

Table category_increments{
id binary
after bigint
increment nigint
category_id bigint [ref: < category_tbl.category_id]
}

Ref team_player_table: team_tbl.team_id <> player_tbl.player_id

// Ref: posts.user_id > user_tbl.id // many-to-one

// Ref: users.id < follows.following_user_id

// Ref: users.id < follows.followed_user_id
