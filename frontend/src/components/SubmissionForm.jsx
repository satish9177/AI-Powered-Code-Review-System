import { useState } from "react";

const starterCode = `public class Calculator {
    public int divide(int a, int b) {
        return a / b;
    }
}`;

export default function SubmissionForm({ onSubmit, isSubmitting }) {
  const [language, setLanguage] = useState("java");
  const [sourceCode, setSourceCode] = useState(starterCode);

  const handleSubmit = async (event) => {
    event.preventDefault();
    await onSubmit({ language, sourceCode });
  };

  return (
    <form className="panel form-panel" onSubmit={handleSubmit}>
      <div className="field-group compact">
        <label htmlFor="language">Language</label>
        <select
          id="language"
          value={language}
          onChange={(event) => setLanguage(event.target.value)}
        >
          <option value="java">Java</option>
          <option value="javascript">JavaScript</option>
          <option value="typescript">TypeScript</option>
          <option value="python">Python</option>
        </select>
      </div>

      <div className="field-group">
        <label htmlFor="sourceCode">Source Code</label>
        <textarea
          id="sourceCode"
          value={sourceCode}
          onChange={(event) => setSourceCode(event.target.value)}
          placeholder="Paste your code here"
          rows={18}
        />
      </div>

      <button type="submit" disabled={isSubmitting || !sourceCode.trim()}>
        {isSubmitting ? "Submitting..." : "Submit for Review"}
      </button>
    </form>
  );
}
