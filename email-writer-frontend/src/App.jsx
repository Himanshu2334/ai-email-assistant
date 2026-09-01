import { useState } from "react";
import "./App.css";

function App() {

  const [emailContent, setEmailContent] = useState("");
  const [tone, setTone] = useState("professional");
  const [generatedEmail, setGeneratedEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [copied, setCopied] = useState(false);
  const [length, setLength] = useState("medium");

  const generateEmail = async () => {

    if (!emailContent.trim()) {
      setError("Please enter an email first.");
      return;
    }

    setLoading(true);
    setError("");
    setGeneratedEmail("");

    try {

      const response = await fetch(
        "http://localhost:8080/api/email/generate",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            emailcontent: emailContent,
            tone: tone,
            length: length
          })
        }
      );

      if (!response.ok) {
        throw new Error("Failed to generate email");
      }

      const data = await response.text();

      setGeneratedEmail(data);

    } catch (error) {

      console.error(error);
      setError("Something went wrong. Please try again.");

    } finally {

      setLoading(false);

    }
  };


const copyToClipboard = () => {

    navigator.clipboard.writeText(generatedEmail);

    setCopied(true);

    setTimeout(() => {
        setCopied(false);
    }, 2000);
};


  const clearAll = () => {

    setEmailContent("");
    setGeneratedEmail("");
    setError("");

  };


  return (

    <div className="app">

      {/* Header */}

      <header className="header">

        <div className="logo">
          ✉
        </div>

        <div>
          <h1>AI Email Writer</h1>
          <p>Generate professional email replies with AI</p>
        </div>

      </header>


      {/* Main Container */}

      <main className="container">


        {/* Left Side */}

        <section className="card">

          <h2>Write Your Email</h2>

          <p className="description">
            Paste the email you received and let AI generate a reply.
          </p>


          {/* Email Input */}

          <label>Original Email</label>

          <textarea
            placeholder="Paste the email you received here..."
            value={emailContent}
            maxLength={5000}
            onChange={(e) => setEmailContent(e.target.value)}
          />

          <div
            className={
              emailContent.length > 4500
                ? "character-count warning"
                : "character-count"
            }
          >
            {emailContent.length} / 5000
          </div>


          {/* Tone */}

          <label>Choose Tone</label>

          <select
            value={tone}
            onChange={(e) => setTone(e.target.value)}
          >

            <option value="professional">
              Professional
            </option>

            <option value="friendly">
              Friendly
            </option>

            <option value="formal">
              Formal
            </option>

            <option value="casual">
              Casual
            </option>

            <option value="apologetic">
              Apologetic
            </option>

          </select>

          {/* Reply Length */}

          <label>Choose Reply Length</label>

          <select
              value={length}
              onChange={(e) => setLength(e.target.value)}
          >
              <option value="short">Short</option>
              <option value="medium">Medium</option>
              <option value="detailed">Detailed</option>
          </select>


          {/* Buttons */}

          <div className="buttons">

            <button
              className="generate-btn"
              onClick={generateEmail}
              disabled={loading}
            >

              {loading ? "Generating..." : "✨ Generate Reply"}

            </button>


            <button
              className="clear-btn"
              onClick={clearAll}
            >

              Clear

            </button>

          </div>


          {/* Error */}

          {error && (
            <p className="error">
              {error}
            </p>
          )}

        </section>


        {/* Right Side */}

        <section className="card">

          <div className="result-header">

            <div>

              <h2>Generated Reply</h2>

              <p className="description">
                Your AI-generated email will appear here.
              </p>

            </div>


              {generatedEmail && (
                <div className="result-buttons">

                  <button
                      className="copy-btn"
                      onClick={copyToClipboard}
                  >
                      {copied ? "✓ Copied!" : "📋 Copy"}
                  </button>

                  <button
                    className="regenerate-btn"
                    onClick={generateEmail}
                    disabled={loading}
                  >
                    🔄 Regenerate
                  </button>

                </div>
              )}

          </div>


          <div className="result-box">

            {loading ? (

              <div className="loading">

                <div className="spinner"></div>

                <p>AI is writing your reply...</p>

              </div>

            ) : generatedEmail ? (

              <div className="generated-text">

                {generatedEmail}

              </div>

            ) : (

              <div className="placeholder">

                <div className="big-icon">✉️</div>

                <p>Your generated reply will appear here</p>

              </div>

            )}

          </div>

        </section>


      </main>


      <footer>

        <p>
          Powered by Spring Boot + Gemini AI
        </p>

      </footer>

    </div>

  );
}

export default App;