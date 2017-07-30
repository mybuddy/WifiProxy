CREATE TABLE static_proxy (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    ssid TEXT,
    hostname TEXT,
    port INTEGER,
    exclusion_hosts TEXT
);