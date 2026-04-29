export default function ReviewResult({ review, error, reviewId }) {
  const isPending = review?.status === "PENDING" || review?.status === "PROCESSING";

  return (
    <section className="panel result-panel">
      <div className="result-header">
        <h2>Review Status</h2>
        {reviewId ? <span className="review-id">ID: {reviewId}</span> : null}
      </div>

      {error ? <p className="error-text">{error}</p> : null}

      {!review && !error ? (
        <p className="muted-text">
          Your submitted review will appear here once the backend accepts it.
        </p>
      ) : null}

      {review ? (
        <>
          <div className={`status-pill status-${review.status?.toLowerCase()}`}>
            {review.status}
          </div>

          {isPending ? (
            <p className="muted-text">
              The review is in the background pipeline. The page is polling every
              2 seconds until Claude finishes.
            </p>
          ) : null}

          {review.status === "FAILED" && review.errorMessage ? (
            <div className="result-box error-box">
              <pre>{review.errorMessage}</pre>
            </div>
          ) : null}

          {review.status === "COMPLETED" && review.reviewResult ? (
            <div className="result-box">
              <pre>{review.reviewResult}</pre>
            </div>
          ) : null}
        </>
      ) : null}
    </section>
  );
}
