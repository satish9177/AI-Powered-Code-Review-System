import { useEffect, useState } from "react";
import SubmissionForm from "./components/SubmissionForm";
import ReviewResult from "./components/ReviewResult";

const POLL_INTERVAL_MS = 2000;

export default function App() {
  const [reviewId, setReviewId] = useState("");
  const [review, setReview] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!reviewId) {
      return undefined;
    }

    let intervalId;
    let cancelled = false;

    const loadReview = async () => {
      try {
        const response = await fetch(`/api/reviews/${reviewId}`);
        if (!response.ok) {
          throw new Error("Unable to fetch review status.");
        }

        const payload = await response.json();
        if (!cancelled) {
          setReview(payload);
          if (payload.status === "COMPLETED" || payload.status === "FAILED") {
            clearInterval(intervalId);
          }
        }
      } catch (fetchError) {
        if (!cancelled) {
          setError(fetchError.message);
          clearInterval(intervalId);
        }
      }
    };

    loadReview();
    intervalId = window.setInterval(loadReview, POLL_INTERVAL_MS);

    return () => {
      cancelled = true;
      clearInterval(intervalId);
    };
  }, [reviewId]);

  const handleSubmit = async ({ language, sourceCode }) => {
    setIsSubmitting(true);
    setError("");
    setReview(null);
    setReviewId("");

    try {
      const response = await fetch("/api/reviews", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ language, sourceCode })
      });

      if (!response.ok) {
        throw new Error("Unable to submit code for review.");
      }

      const payload = await response.json();
      setReviewId(payload.reviewId);
      setReview({
        id: payload.reviewId,
        status: payload.status
      });
    } catch (submitError) {
      setError(submitError.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <main className="page">
      <section className="hero">
        <div>
          <h1>AI-Powered Code Review System</h1>
          <p>
            Submit code, hand the heavy lifting to the async pipeline, and get a
            Claude-powered review back without freezing the UI.
          </p>
        </div>
      </section>

      <section className="workspace">
        <SubmissionForm onSubmit={handleSubmit} isSubmitting={isSubmitting} />
        <ReviewResult review={review} error={error} reviewId={reviewId} />
      </section>
    </main>
  );
}
