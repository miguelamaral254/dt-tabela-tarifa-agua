package gruporas.dttabelatarifaagua.shared.usecase;

public interface UseCase<I, O> {
    O execute(I i);
}
