public class Country {
    private String name;
    private String abbreviation;

    public Country(String name, String abbreviation) {
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return abbreviation.equals(country.abbreviation) && name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return abbreviation.hashCode();
    }

    @Override
    public String toString() {
        return "Country{" + "name='" + name + '\'' + ", abbreviation='" + abbreviation + '\'' + '}';
    }
}
